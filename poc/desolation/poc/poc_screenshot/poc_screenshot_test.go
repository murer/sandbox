package poc_screenshot_test

import (
	"testing"

	"github.com/stretchr/testify/assert"

	"github.com/murer/desolation/poc/poc_screenshot"
)

func TestNoop(t *testing.T) {
	assert.Equal(t, 1, poc_screenshot.Noop())

}
