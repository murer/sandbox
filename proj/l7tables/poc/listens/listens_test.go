package listens

import (
	"net"
	"testing"

	"github.com/murer/l7tables/util"
)

func TestListen(t *testing.T) {
	server, err := net.Listen("tcp4", "127.0.0.1:5000")
	util.Check(err)
	defer server.Close()
	// conn, err := server.Accept()
	// util.Check(err)
	// defer conn.Close()
	// io.ReadAll(conn)

	// assert.Equal(t, "OK", Ok())
}
