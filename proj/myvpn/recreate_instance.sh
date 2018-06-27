#!/bin/bash -xe

gcloud compute instances list \
  --project frotanetappdevel --zones southamerica-east1-a

gcloud compute disks list \
  --project frotanetappdevel --zones southamerica-east1-a

gcloud compute instances delete myvpn -q \
  --project frotanetappdevel --zone southamerica-east1-a \
  || true

gcloud compute instances create myvpn \
  --project frotanetappdevel --zone southamerica-east1-a \
  --image-project=ubuntu-os-cloud \
  --image-family=ubuntu-1604-lts \
  --can-ip-forward \
  --boot-disk-auto-delete \
  --address=35.198.12.49
