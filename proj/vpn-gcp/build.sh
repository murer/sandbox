#!/bin/bash -xe

cmd_image() {
    # --image-project=ubuntu-os-cloud \
    # --image-family=ubuntu-1804-lts \

  gcloud compute instances create myvpn-1 myvpn-2 \
    --project dsavault --zone us-east1-b \
    --machine-type f1-micro \
    --image-project=debian-cloud \
    --image-family=debian-10 \
    --can-ip-forward
}


cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
