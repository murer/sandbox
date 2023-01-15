package util

import (
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestOk(t *testing.T) {
	assert.Equal(t, "OK", Ok())
}

func BenchmarkOk(b *testing.B) {
	for i := 0; i < b.N; i++ {
		Ok()
	}
}
