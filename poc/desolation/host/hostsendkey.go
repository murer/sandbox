package host

import (
	"context"
	"os/exec"
	"time"

	"github.com/murer/desolation/util"
)

// xdotool windowactivate --sync 31457295 key l s Return

func HostSendKeys(keys []string) {
	ctx, cancel := context.WithTimeout(context.Background(), 1000*time.Millisecond)
	defer cancel()
	args := append([]string{"key"}, keys...)
	cmd := exec.CommandContext(ctx, "xdotool", args...)
	cmd.Start()
	err := cmd.Wait()
	util.Check(err)
}
