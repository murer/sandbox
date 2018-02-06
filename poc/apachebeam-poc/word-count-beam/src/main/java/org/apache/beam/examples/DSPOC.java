package org.apache.beam.examples;

import java.io.Serializable;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

public class DSPOC {

	public static class Company implements Serializable {
		String id;
		String name;

		@Override
		public String toString() {
			return "[Company id=" + id + ", name=" + name + "]";
		}

	}

	public static class Cargo implements Serializable {
		String id;
		String companyId;

		@Override
		public String toString() {
			return "[Cargo id=" + id + ", companyId=" + companyId + "]";
		}

	}

	public static void main(String[] args) {

		PipelineOptions options = PipelineOptionsFactory.create();
		Pipeline p = Pipeline.create(options);

		final TupleTag<KV<String, Company>> companyTag = new TupleTag<>();
		final TupleTag<KV<String, Cargo>> cargoTag = new TupleTag<>();

		PCollection<String> c = p.apply(TextIO.read().from("sample/input.csv"));
		PCollectionTuple c2 = c.apply(ParDo.of(new DoFn<String, KV<String, Company>>() {
			@ProcessElement
			public void processElement(ProcessContext c) {
				String[] words = c.element().split(",");
				if ("company".equals(words[0])) {
					Company company = new Company();
					company.id = words[1];
					company.name = words[2];
					c.output(companyTag, KV.of(company.id, company));
				} else {
					Cargo cargo = new Cargo();
					cargo.id = words[1];
					cargo.companyId = words[2];
					c.output(cargoTag, KV.of(cargo.companyId, cargo));
				}
			}
		}).withOutputTags(companyTag, TupleTagList.of(cargoTag)));

		PCollection<KV<String, Company>> comps = c2.get(companyTag);
		PCollection<KV<String, Cargo>> cargos = c2.get(cargoTag);

		KeyedPCollectionTuple<String> c3 = KeyedPCollectionTuple.of(new TupleTag<Company>(), comps).and(new TupleTag<Cargo>(), cargos);

		// PCollection<String> c3 = c2.apply(ParDo.of(new DoFn<KV<String, Company>,
		// String>() {
		// @ProcessElement
		// public void processElement(ProcessContext c) {
		// c.output(c.element().toString());
		// }
		// }));

		// c = c.apply(Combine.globally(new CombineFn<String, Map<String, Long>,
		// String>() {
		//
		// public Map<String, Long> createAccumulator() {
		// return new HashMap<String, Long>();
		// }
		//
		// @Override
		// public Map<String, Long> addInput(Map<String, Long> accumulator, String
		// input) {
		// Long num = accumulator.get(input);
		// accumulator.put(input, num == null ? 1L : num + 1L);
		// return accumulator;
		// }
		//
		// @Override
		// public Map<String, Long> mergeAccumulators(Iterable<Map<String, Long>>
		// accumulators) {
		// Map<String, Long> ret = new HashMap<String, Long>();
		// for (Map<String, Long> accumulator : accumulators) {
		// for (Entry<String, Long> entry : accumulator.entrySet()) {
		// Long num = ret.get(entry.getKey());
		// ret.put(entry.getKey(), num == null ? entry.getValue() : num +
		// entry.getValue());
		// }
		// }
		// return ret;
		// }
		//
		// @Override
		// public String extractOutput(Map<String, Long> accumulator) {
		// return accumulator.toString();
		// }
		// }));
		// PDone done = c3.apply(TextIO.write().to("target/poc/output"));

		p.run().waitUntilFinish();
	}
}
