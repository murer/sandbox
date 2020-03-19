#!/bin/bash -xe

cmd_network_delete() {
  gcloud compute firewall-rules delete neta-allow-basic -q &
  gcloud compute firewall-rules delete netb-allow-basic -q &
  wait
  gcloud compute networks delete neta --project dsavault -q &
  gcloud compute networks delete netb --project dsavault -q &
  wait
}

cmd_network_create() {
  gcloud compute networks create neta --project dsavault --subnet-mode auto &
  gcloud compute networks create netb --project dsavault --subnet-mode auto &
  wait
  gcloud compute firewall-rules create neta-allow-basic --network neta --allow tcp:22,tcp:3389,icmp &
  gcloud compute firewall-rules create netb-allow-basic --network netb --allow tcp:22,tcp:3389,icmp &
  wait
}

cmd_instance_delete() {
  gcloud compute instances delete myvpn-u1 --project dsavault --zone us-east1-b -q &
  gcloud compute instances delete myvpn-u2 --project dsavault --zone us-east1-b -q &
  wait %1 && wait %2
}

cmd_instance_create() {
    # --image-project=ubuntu-os-cloud \
    # --image-family=ubuntu-1804-lts \
    # --image-project=debian-cloud \
    # --image-family=debian-10 \

  gcloud compute instances create myvpn-u1 myvpn-u2 \
    --project dsavault --zone us-east1-b \
    --machine-type f1-micro \
    --image-project=ubuntu-os-cloud \
    --image-family=ubuntu-1804-lts \
    --can-ip-forward
}

cmd_recreate() {
  cmd_instance_delete
  cmd_network_delete
  cmd_network_create
  cmd_instance_create
}


cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
