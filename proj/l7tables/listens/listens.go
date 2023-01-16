package listens

import (
	"net"

	"github.com/murer/l7tables/util"
)

type LTListens struct {
	listeners []*LTListener
}

func (me *LTListens) Listen(proto string, address string) {
	server, err := net.Listen(proto, address)
	util.Check(err)
	listener := createListener(&server)
	me.listeners = append(me.listeners, listener)
}

func (me *LTListens) Close() {
	for _, listener := range me.listeners {
		listener.Close()
	}
}

func Create() *LTListens {
	return &LTListens{}
}
