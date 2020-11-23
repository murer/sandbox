package poc_qrcode

import (
	"context"
	"os/exec"
	"time"
)

// zbarimg --raw poc/poc_qrcode/sample_qrcode.png  2>/dev/null | xxd

func Parse() string {
	ctx, cancel := context.WithTimeout(context.Background(), 1000*time.Millisecond)
	defer cancel()
	cmd := exec.CommandContext(ctx, "zbarimg", "--raw", "sample_qrcode.png")
	out, err := cmd.Output()
	if err != nil {
		panic(err)
	}
	return string(out)
}
