package org.apache.beam.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Combine.CombineFn;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
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

		PCollection<String> c = p.apply(TextIO.read().from("pom.xml"));
		c = c.apply(ParDo.of(new DoFn<String, String>() {
			@ProcessElement
			public void processElement(ProcessContext c) {
				String[] array = c.element().split("[^A-Za-z0-9]+");
				for (String word : array) {
					c.output(word);
				}
			}
		}));
		c = c.apply(Combine.globally(new CombineFn<String, Map<String, Long>, String>() {

			public Map<String, Long> createAccumulator() {
				return new HashMap<String, Long>();
			}

			@Override
			public Map<String, Long> addInput(Map<String, Long> accumulator, String input) {
				Long num = accumulator.get(input);
				accumulator.put(input, num == null ? 1L : num + 1L);
				return accumulator;
			}

			@Override
			public Map<String, Long> mergeAccumulators(Iterable<Map<String, Long>> accumulators) {
				Map<String, Long> ret = new HashMap<String, Long>();
				for (Map<String, Long> accumulator : accumulators) {
					for (Entry<String, Long> entry : accumulator.entrySet()) {
						Long num = ret.get(entry.getKey());
						ret.put(entry.getKey(), num == null ? entry.getValue() : num + entry.getValue());
					}
				}
				return ret;
			}

			@Override
			public String extractOutput(Map<String, Long> accumulator) {
				return accumulator.toString();
			}
		}));
		PDone done = c.apply(TextIO.write().to("target/poc/output"));

		p.run().waitUntilFinish();
	}
}
