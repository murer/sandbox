package listens

import (
	"fmt"
	"log"
	"net"

	"github.com/murer/l7tables/util"
)

type LTListener struct {
	listener net.Listener
}

func createListener(listener *net.Listener) *LTListener {
	return &LTListener{
		listener: *listener,
	}
}

func (me *LTListener) Accepts(ch chan net.Conn) {
	for {
		log.Printf("Waiting for connection: %s", me.String())
		conn, err := me.listener.Accept()
		util.Check(err)
		log.Printf("Connection accepted: %s, %s", me.String(), conn.RemoteAddr().String())
		ch <- conn
		log.Printf("bbbb")
	}
}

func (me *LTListener) Close() {
	util.Check(me.listener.Close())
}

func (me *LTListener) String() string {
	return fmt.Sprintf("%s:%s", me.listener.Addr().Network(), me.listener.Addr().String())
}
