package host_test

import (
	"testing"

	"github.com/murer/desolation/host"
	"github.com/murer/desolation/message"
	"github.com/stretchr/testify/assert"
)

func TestCapture(t *testing.T) {
	msg := host.Capture()
	assert.Equal(t, &message.Message{Name: "nocode", Headers: map[string]string(nil), Payload: ""}, msg)
}
