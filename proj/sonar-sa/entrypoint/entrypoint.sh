#!/bin/bash

set -xeuo pipefail

#chown -R "$(id -u):$(id -g)" data extensions logs temp
chmod -R 700 data extensions logs temp

exec "$@"
