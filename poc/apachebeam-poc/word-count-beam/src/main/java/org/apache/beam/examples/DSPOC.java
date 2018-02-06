package org.apache.beam.examples;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;

public class DSPOC {

	public static PTransform<PInput, POutput> noop() {
		return new PTransform<PInput, POutput>() {
			private static final long serialVersionUID = 1L;

			public POutput expand(PInput in) {
				return (POutput) in;
			}
		};
	}

	public static void main(String[] args) {

		PipelineOptions options = PipelineOptionsFactory.create();
		Pipeline p = Pipeline.create(options);

		PCollection<String> c1 = p.apply(TextIO.read().from("pom.xml"));
		PCollection<String> c2 = (PCollection<String>) c1.apply(noop());
		System.out.println("c1 == c2: " + (c1 == c2));
		PDone done = c2.apply(TextIO.write().to("target/poc/output"));

		p.run().waitUntilFinish();
	}
}
