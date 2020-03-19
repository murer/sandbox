#!/bin/bash -xe

cmd_network_delete() {
  gcloud compute firewall-rules delete neta-allow-basic -q &
  gcloud compute firewall-rules delete netb-allow-basic -q &
  gcloud beta compute networks subnets delete neta-main --project dsavault -q --region us-east1 &
  gcloud beta compute networks subnets delete netb-main --project dsavault -q --region us-east1 &
  wait
  gcloud compute networks delete neta --project dsavault -q &
  gcloud compute networks delete netb --project dsavault -q &
  wait
}

cmd_network_create() {
  gcloud compute networks create neta --project dsavault --subnet-mode custom &
  gcloud compute networks create netb --project dsavault --subnet-mode custom &
  wait
  gcloud beta compute networks subnets create neta-main --project dsavault \
    --network neta --region us-east1 \
    --range=10.0.20.0/24 --enable-private-ip-google-access &
  gcloud beta compute networks subnets create netb-main --project dsavault \
    --network neta --region us-east1 \
    --range=10.1.20.0/24 --enable-private-ip-google-access &
  gcloud compute firewall-rules create neta-allow-basic --network neta --allow tcp:22,tcp:3389,icmp &
  gcloud compute firewall-rules create netb-allow-basic --network netb --allow tcp:22,tcp:3389,icmp &
  wait
}

cmd_instance_delete() {
  gcloud compute instances delete ivpn-neta-1 --project dsavault --zone us-east1-b -q &
  gcloud compute instances delete ivpn-neta-2 --project dsavault --zone us-east1-b -q &
  gcloud compute instances delete ivpn-netb-1 --project dsavault --zone us-east1-b -q &
  gcloud compute instances delete ivpn-netb-2 --project dsavault --zone us-east1-b -q &
  wait
}

instance_create() {
  # --image-project=ubuntu-os-cloud \
  # --image-family=ubuntu-1804-lts \
  # --image-project=debian-cloud \
  # --image-family=debian-10 \

  inst_name="${1?'instance name'}"
  shift
  gcloud compute instances create "$inst_name" \
    --project dsavault --zone us-east1-b \
    --machine-type f1-micro \
    --image-project=ubuntu-os-cloud \
    --image-family=ubuntu-1804-lts \
    --can-ip-forward \
    "$@"
}

cmd_instance_create() {
  instance_create ivpn-neta-1 --network-interface network=neta,subnet=neta-main &
  instance_create ivpn-neta-2 --network-interface network=neta,subnet=neta-main &
  instance_create ivpn-netb-1 --network-interface network=neta,subnet=netb-main &
  instance_create ivpn-netb-2 --network-interface network=neta,subnet=netb-main &
  wait
}

cmd_recreate() {
  cmd_instance_delete
  cmd_network_delete
  cmd_network_create
  cmd_instance_create
}

check_access() {
  gcloud compute ssh "${1?'source'}" -- nc -zvw 5 "${2?'dest'}" 22
}

cmd_test() {
  check_access ivpn-neta-suba-1 ivpn-neta-subb-1
  check_access ivpn-neta-suba-1 ivpn-netb-def-1
  # check_access ivpn-neta-suba-1 ivpn-neta-suba-2
  # check_access ivpn-neta-suba-2 ivpn-neta-suba-1
  # check_access ivpn-neta-subb-1 ivpn-neta-subb-2
  # check_access ivpn-neta-subb-2 ivpn-neta-subb-1
  # check_access ivpn-netb-def-1 ivpn-netb-def-2
  # check_access ivpn-netb-def-2 ivpn-netb-def-1
  echo "Success"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
