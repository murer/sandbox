#!/bin/bash -e

cmd_clean() {
  rm -rf build || true
}

cmd_test() {
  go test ./...
}

cmd_vendor() {
  go mod vendor -v
}

cmd_fmt() {
  go fmt "$@"
}

cmd_build() {
  l7tables_goos="${1?'use: linux, darwin or windows'}"
  l7tables_goarch="${2:-"amd64"}"
  l7tables_version="${3:-"dev"}"
  l7tables_excname="l7tables"
  if [[ "x$l7tables_goos" == "xwindows" ]]; then l7tables_excname="l7tables.exe"; fi
  l7tables_ldflags="-s -w -extldflags '-static' -X main.Version=$l7tables_version"
  rm -rf "build/out/$l7tables_goos-$l7tables_goarch" || true
  rm "build/pack/l7tables-$l7tables_goos-$l7tables_goarch-$l7tables_version.tar.gz" || true
  mkdir -p build/pack
  CGO_ENABLED="0" GOOS="$l7tables_goos" GOARCH="$l7tables_goarch" \
    go build -a -trimpath -ldflags "$l7tables_ldflags" \
      -installsuffix cgo -tags netgo -mod mod \
      -o "build/out/$l7tables_goos-$l7tables_goarch/l7tables/$l7tables_excname" .
  du -hs "build/out/$l7tables_goos-$l7tables_goarch/l7tables/$l7tables_excname"
  file "build/out/$l7tables_goos-$l7tables_goarch/l7tables/$l7tables_excname"
  cd "build/out/$l7tables_goos-$l7tables_goarch"
  tar cvzf "../../pack/l7tables-$l7tables_goos-$l7tables_goarch-$l7tables_version.tar.gz" "l7tables"
  cd -
}

cmd_build_all() {
  l7tables_version="${1?"version"}"
  rm -rf "build/out" "build/pack" || true
  cmd_build windows amd64 "$l7tables_version"
  cmd_build darwin amd64 "$l7tables_version"
  cmd_build linux amd64 "$l7tables_version"
}

cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"

