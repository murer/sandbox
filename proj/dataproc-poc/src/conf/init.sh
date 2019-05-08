#!/bin/bash -xe

curl https://dl.google.com/cloudagents/install-monitoring-agent.sh | sudo bash

# dataproc_sandbox_role="$(/usr/share/google/get_metadata_value attributes/dataproc-role)"

# adduser --disabled-password --gecos sandbox sandbox
# groupadd -r supersudo && echo "%supersudo ALL=(ALL:ALL) NOPASSWD: ALL" > /etc/sudoers.d/supersudo
# usermod -a -G supersudo sandbox
#
# if [[ "$dataproc_sandbox_role" == "Master" ]]; then
#   apt-get update -y
#   apt-get install -y python3-pip
#
#   pip3 install jupyter spylon-kernel
#   python3 -m spylon_kernel install
#   jupyter --version
#
#   cd /home/sandbox
#   sudo -u sandbox mkdir logs
#   sudo -u sandbox jupyter notebook \
#     --ip=0.0.0.0 --port=8085 --NotebookApp.token='' \
#     1> logs/jupyter.log 2>&1 &
#   cd -
# fi
