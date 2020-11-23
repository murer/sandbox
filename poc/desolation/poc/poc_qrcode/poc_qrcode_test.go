package poc_qrcode_test

import (
	"testing"

	"github.com/murer/desolation/poc/poc_qrcode"
	"github.com/stretchr/testify/assert"
)

func TestScreenshot(t *testing.T) {
	poc_qrcode.Parse()
	assert.Equal(t, 1, 1)
}
