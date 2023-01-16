package listens

import "net"

type LTListener struct {
}

func createListener(conn *net.Listener) *LTListener {
	return nil
}

func (me *LTListener) Close() {

}
