package poc_screenshot_test

import (
	"testing"

	"github.com/stretchr/testify/assert"

	"github.com/murer/desolation/poc/poc_screenshot"
)

func TestScreenshot(t *testing.T) {
	out := poc_screenshot.Screenshot()
	assert.Equal(t, 1, 1)
	assert.Less(t, 1, len(out))
}
