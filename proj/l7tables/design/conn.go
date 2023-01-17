package design

import (
	"bufio"
	"bytes"
	"net/http"

	"github.com/murer/l7tables/util"
)

type Conn interface {
	Close() error
	Retrieved() []byte
	Writable() []byte

	// LocalAddr() Addr
	// RemoteAddr() Addr
	// SetDeadline(t time.Time) error
	// SetReadDeadline(t time.Time) error
	// SetWriteDeadline(t time.Time) error
}

func httpIdentifier(src Conn) {
	data := src.Retrieved()
	index := bytes.Index(data, []byte("\n\n"))
	if index < 0 {
		index = bytes.Index(data, []byte("/r/n/r/n"))
	}
	if index < 0 {
		return I_NEED_MORE_DATA
	}
	req, err := http.ReadRequest(bufio.NewReader(bytes.NewBuffer(data)))
	util.Check(err)

}
