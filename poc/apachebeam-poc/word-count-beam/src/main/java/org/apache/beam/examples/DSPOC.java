package org.apache.beam.examples;

import java.io.Serializable;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.PCollectionView;
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

	public static class CompCargo implements Serializable {
		private Cargo cargo;
		private Company company;

		@Override
		public String toString() {
			return "[CompCargo cargo=" + cargo + ", company=" + company + "]";
		}

	}

	public static void main(String[] args) {

		PipelineOptions options = PipelineOptionsFactory.create();
		Pipeline p = Pipeline.create(options);

		final TupleTag<KV<String, Serializable>> companyTag = new TupleTag<>();
		final TupleTag<KV<String, Serializable>> cargoTag = new TupleTag<>();

		PCollection<String> c = p.apply(TextIO.read().from("sample/input.simple.csv"));
		PCollectionTuple c2 = c.apply(ParDo.of(new DoFn<String, KV<String, Serializable>>() {
			@ProcessElement
			public void processElement(ProcessContext c) {
				String[] words = c.element().split(",");
				if ("company".equals(words[0])) {
					Company company = new Company();
					company.id = words[1];
					company.name = words[2];
					c.output(KV.of(company.id, (Serializable) company));
				} else {
					Cargo cargo = new Cargo();
					cargo.id = words[1];
					cargo.companyId = words[2];
					c.output(cargoTag, KV.of(cargo.companyId, (Serializable) cargo));
				}
			}
		}).withOutputTags(companyTag, TupleTagList.of(cargoTag)));

		final PCollection<KV<String, Serializable>> companies = c2.get(companyTag);
		PCollection<KV<String, Serializable>> cargos = c2.get(cargoTag);
		cargos.setCoder(companies.getCoder());

		final PCollectionView<Iterable<KV<String, Serializable>>> sideInput = companies
				.apply(View.<KV<String, Serializable>>asIterable());

		PCollection<String> c4 = cargos.apply(ParDo.of(new DoFn<KV<String, Serializable>, String>() {
			@ProcessElement
			public void processElement(ProcessContext c) {
				Iterable<KV<String, Serializable>> comps = c.sideInput(sideInput);
				for (KV<String, Serializable> company : comps) {
					CompCargo ret = new CompCargo();
					ret.cargo = (Cargo) c.element().getValue();
					ret.company = (Company) company.getValue();
					if (ret.company.id.equals(ret.cargo.companyId)) {
						c.output(ret.toString());
					}
				}
			}
		}).withSideInputs(sideInput));

		c4.apply(TextIO.write().to("target/poc/result"));

		p.run().waitUntilFinish();
	}

}
