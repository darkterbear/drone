def push8(buf, val):
    buf.append(0xFF & val)


def push16(buf, val):
    # low byte
    push8(buf, val)

    # high byte
    push8(buf, val >> 8)
