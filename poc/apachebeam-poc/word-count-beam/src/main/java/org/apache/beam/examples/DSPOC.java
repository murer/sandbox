package org.apache.beam.examples;

import java.io.Serializable;
import java.util.List;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
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

		final TupleTag<KV<String, Serializable>> companyTag = new TupleTag<>();
		final TupleTag<KV<String, Serializable>> cargoTag = new TupleTag<>();

		PCollection<String> c = p.apply(TextIO.read().from("sample/input.csv"));
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

		PCollection<KV<String, Serializable>> companies = c2.get(companyTag);
		PCollection<KV<String, Serializable>> cargos = c2.get(cargoTag);
		cargos.setCoder(companies.getCoder());

		System.out.println(companies.getCoder());
		System.out.println(cargos.getCoder());

		final TupleTag<Serializable> compT = new TupleTag<Serializable>();
		final TupleTag<Serializable> cargoT = new TupleTag<Serializable>();
		KeyedPCollectionTuple<String> c3 = KeyedPCollectionTuple.of(compT, companies).and(cargoT, cargos);

		PCollection<KV<String, CoGbkResult>> c4 = c3.apply(CoGroupByKey.<String>create());

		c4.apply(ParDo.of(new DoFn<KV<String, CoGbkResult>, String>() {
			@ProcessElement
			public void processElement(ProcessContext c) {
				KV<String, CoGbkResult> e = c.element();
				String name = e.getKey();
				Iterable<Serializable> emailsIter = e.getValue().getAll(compT);
				List<Serializable> phonesIter = (List<Serializable>) e.getValue().getAll(cargoT);
				String result = String.format("name: '%s', emailsIter: '%s', phonesIter: '%s'", name,
						emailsIter, phonesIter.size());
				System.out.println(result);
				c.output(result);
			}
		}));

		// c3.apply(CoGroupByKey.<String> create());

		// final List<KV<String, String>> emailsList = Arrays.asList(KV.of("amy",
		// "amy@example.com"),
		// KV.of("carl", "carl@example.com"), KV.of("julia", "julia@example.com"),
		// KV.of("carl", "carl@email.com"));
		//
		// final List<KV<String, String>> phonesList = Arrays.asList(KV.of("amy",
		// "111-222-3333"),
		// KV.of("james", "222-333-4444"), KV.of("amy", "333-444-5555"), KV.of("carl",
		// "444-555-6666"));
		//
		// PCollection<KV<String, String>> emails = p.apply("CreateEmails",
		// Create.of(emailsList));
		// PCollection<KV<String, String>> phones = p.apply("CreatePhones",
		// Create.of(phonesList));
		//
		// final TupleTag<String> emailsTag = new TupleTag();
		// final TupleTag<String> phonesTag = new TupleTag();
		//
		// PCollection<KV<String, CoGbkResult>> results =
		// KeyedPCollectionTuple.of(emailsTag, emails)
		// .and(phonesTag, phones).apply(CoGroupByKey.<String>create());
		//
		// PCollection<String> contactLines = results.apply(ParDo.of(new DoFn<KV<String,
		// CoGbkResult>, String>() {
		// @ProcessElement
		// public void processElement(ProcessContext c) {
		// KV<String, CoGbkResult> e = c.element();
		// String name = e.getKey();
		// Iterable<String> emailsIter = e.getValue().getAll(emailsTag);
		// Iterable<String> phonesIter = e.getValue().getAll(phonesTag);
		// String result = String.format("name: '%s', emailsIter: '%s', phonesIter:
		// '%s'", name, emailsIter,
		// phonesIter);
		// System.out.println(result);
		// c.output(result);
		// }
		// }));

		p.run().waitUntilFinish();
	}

}
