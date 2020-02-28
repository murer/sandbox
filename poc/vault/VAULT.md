# vault


```shell
# Login

vault login
vault login --service service-account.json
vault login --gce

# Create secret version

vault create dsa --secret 'mysecret' \
  --share 'user:juquinha@dextra-sw.com' 'user:mariazinha@dextra-sw.com'

vault create juquinha --secret 'mysecret' \
  --share 'group:dsa@dextra-sw.com'

# Read secret

vault get dsa 'any/secret'
vault get dsa 'any/secret' --rev 18253
vault get juquinha 'any/secret'

# Delete secret

vault delete dsa 'any/secret'
vault delete dsa 'any/secret' --rev 18253
vault delete juquinha 'any/secret'

# List secrets
vault ls dsa
# -> any/secret
# -> other/xpto

# List versions
vault ls dsa 'any/secret'
# -> 1232
# -> 1345

vault share get dsa 'any/secret'
# user:mariazinha@dextra-sw.com
# user:juquinha@dextra-sw.com

vault share save dsa 'any/secret' <<-EOF
user:mariazinha@dextra-sw.com
user:juquinha@dextra-sw.com
EOF
