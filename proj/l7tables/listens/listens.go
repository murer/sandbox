package listens

import (
	"log"
	"net"

	"github.com/murer/l7tables/util"
)

type LTListens struct {
	listeners []*LTListener
	accepts   chan *net.Conn
}

func (me *LTListens) Listen(proto string, address string) {
	log.Printf("Listening %s:%s", proto, address)
	server, err := net.Listen(proto, address)
	util.Check(err)
	listener := createListener(&server)
	me.listeners = append(me.listeners, listener)
}

func (me *LTListens) Accepts() {
	for _, listener := range me.listeners {
		go listener.Accepts(me.accepts)
	}
}

func (me *LTListens) Close() {
	for _, listener := range me.listeners {
		log.Printf("Closing listener %s", listener)
		listener.Close()
	}
}

func Create() *LTListens {
	return &LTListens{}
}
