#!/bin/bash -xe

cmd_clean() {
    rm -rf target || true
}

cmd_data() {
    mkdir -p target/files
    echo a > target/files/a.txt
    echo a > target/files/b.txt
    mkdir target/files/x
    echo c > target/files/x/c.txt
}

cmd_gpg_create_key() {
    mkdir -p target/gpgkeys
    gpg --homedir target/gpgkeys --batch --generate-key keydata-a.txt
    gpg --homedir target/gpgkeys --batch --generate-key keydata-b.txt
}

cmd_gpg_create_quick_key() {
    mkdir -p target/gpgkeys
    gpg --homedir target/gpgkeys --batch --passphrase '' --quick-gen-key aaaaaa rsa4096
    gpg --homedir target/gpgkeys --batch --passphrase '' --quick-gen-key bbbbbb rsa4096

    echo "
    addkey
    4
    4096
    0
    y
    y
    save
    " | GPG_TTY=$(tty) gpg --homedir target/gpgkeys --command-fd=0 --status-fd=1 --edit-key aaaaaa

    echo "
    addkey
    4
    4096
    0
    y
    y
    save
    " | gpg --homedir target/gpgkeys --command-fd=0 --status-fd=1 --edit-key bbbbbb
}

cmd_gpg_list_key() {
    gpg --home target/gpgkeys --list-keys
    gpg --home target/gpgkeys --list-public-keys
    gpg --home target/gpgkeys --list-secret-keys
}

cmd_gpg_export_key() {
    rm -rf target/keys || true
    mkdir -p target/keys
    gpg --home target/gpgkeys --export-secret-keys --armor pyrata > target/keys/pyrata.private.key
    gpg --home target/gpgkeys --export --armor pyrata > target/keys/pyrata.public.key
}

cmd_gpg_reimport_public_key() {
    rm -rf target/gpgkeys
    mkdir -p target/gpgkeys
    gpg --home target/gpgkeys --import target/keys/pyrata.public.key
}

cmd_gpg_reimport_private_key() {
    rm -rf target/gpgkeys
    mkdir -p target/gpgkeys
    gpg --home target/gpgkeys --import target/keys/pyrata.private.key
}

cmd_gpg_key_encrypt() {
    true
}

cmd_test() {
    cmd_clean
    cmd_data
    cmd_gpg_create_quick_key
    cmd_gpg_list_key
    cmd_gpg_export_key

    # cmd_gpg_reimport_public_key
    # cmd_gpg_list_key

    # cmd_gpg_reimport_private_key
    # cmd_gpg_list_key
    
    echo SUCCESS
}



cd "$(dirname "$0")"; _cmd="${1?"cmd is required"}"; shift; "cmd_${_cmd}" "$@"
