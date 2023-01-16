package listens

import (
	"fmt"
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

func (me *LTListener) Close() {
	util.Check(me.listener.Close())
}

func (me *LTListener) String() string {
	return fmt.Sprintf("%s:%s", me.listener.Addr().Network(), me.listener.Addr().String())
}
