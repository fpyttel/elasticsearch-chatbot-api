package de.fpyttel.teams.bot.parser.boundary;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import de.fpyttel.teams.bot.client.ms.entity.Action;
import de.fpyttel.teams.bot.client.ms.entity.Environment;
import de.fpyttel.teams.bot.parser.entity.Category;
import de.fpyttel.teams.bot.parser.entity.CategoryType;
import de.fpyttel.teams.bot.parser.entity.Message;
import de.fpyttel.teams.bot.parser.entity.Message.Status;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;

@Component
@Slf4j
public class ActionMessageParser {

	private static DoccatModel categorizerModel;
	private static DocumentCategorizerME docCategorizer;
	private static SentenceDetectorME sentenceCategorizer;
	private static TokenizerME tokenizer;
	private static POSTaggerME posTagger;
	private static LemmatizerME lemmatizer;

	static {
		try {
			// get trained model
			categorizerModel = getTrainedCategorizerModel();
			// get categorizer
			docCategorizer = new DocumentCategorizerME(categorizerModel);
			// get sentence detector
			sentenceCategorizer = new SentenceDetectorME(
					new SentenceModel(new FileInputStream(ResourceUtils.getFile("classpath:nlp/en-sent.bin"))));
			// get tokenizer
			tokenizer = new TokenizerME(
					new TokenizerModel(new FileInputStream(ResourceUtils.getFile("classpath:nlp/en-token.bin"))));
			// get part-of-speech tagger
			posTagger = new POSTaggerME(
					new POSModel(new FileInputStream(ResourceUtils.getFile("classpath:nlp/en-pos-maxent.bin"))));
			// get lemmatizer
			lemmatizer = new LemmatizerME(
					new LemmatizerModel(new FileInputStream(ResourceUtils.getFile("classpath:nlp/en-lemmatizer.bin"))));
		} catch (IOException e) {
			throw new RuntimeException("Cound not init all NLP objects", e);
		}
	}

	private static DoccatModel getTrainedCategorizerModel() throws IOException {
		log.info("start training the categorizer model");
		final InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(
				ResourceUtils.getFile("classpath:nlp/elastic-categorizer.txt"));
		final ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
		final ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

		final DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

		final TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
		params.put(TrainingParameters.CUTOFF_PARAM, 0);
		params.put(AbstractTrainer.VERBOSE_PARAM, false);

		final DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
		log.info("finished categorizer model training");
		return model;
	}

	public Message parse(@NonNull final Action action) {
		// get text
		final String text = action.getText().replaceAll("\\<.*?\\>", "");

		// define return values
		Environment env = null;
		Category category = null;
		Status status = Status.incomplete;

		String[] sentences = sentenceCategorizer.sentDetect(text);
		for (String sentence : sentences) {
			// execute NLP logic to finally calculate the category
			final String[] tokens = tokenizer.tokenize(sentence);
			final String[] posTags = posTagger.tag(tokens);
			final String[] lemmas = lemmatizer.lemmatize(tokens, posTags);
			category = Category
					.valueOf(docCategorizer.getBestCategory(docCategorizer.categorize(lemmas)));
			log.info("parsed action with message category=[{}]", category);

			// continue based on category & type
			if (CategoryType.log == CategoryType.valueOf(category)) {
				env = searchEnv(sentence);
			}

			// check for completeness
			if (CategoryType.log == CategoryType.valueOf(category)) {
				if (env == null) {
					category = Category.log_request_continue;
					status = Status.incomplete;
				} else {
					category = Category.log_request;
					status = Status.complete;
				}
			}
		}

		return Message.builder().category(category).status(status).environment(env).build();
	}

	private Environment searchEnv(final String text) {
		for (Environment env : Environment.class.getEnumConstants()) {
			// search for any match
			if ((" " + text + " ").toLowerCase().matches(".*\\s" + env.toString().toLowerCase() + "\\s.*")) {
				return env;
			}
		}
		return null;
	}

}
