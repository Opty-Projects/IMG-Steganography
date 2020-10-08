# IMG-Steganography

Context:
- `M`: Secret Message
- `I`: Image Where `M` is gonna be Wrapped
- `W`: Image Where `M` is Wrapped
- `K`: Key to Wrap `M` into `I` & to Unwrap `M` out of `W`

It allows to:
- Generate `K`.
- Wrap `M` into `W` using Random LSBs.
- Unwrap `M` out of `W`.

---

### Generating `K`:

Command:
- `mvn exec:java -Dexec.class="LSBKeyGenerator" -Dexec.args="{0} {1} {2} {3}"`

Arguments:
- `{0}`: Min `I` Width
- `{1}`: Min `I` Height
- `{2}`: Number of Bytes in `M` (Size of `M` File)
- `{3}`: Pathname to Store `K` (`K` Filename: `{3}/LSB.key`)

---

### Wrapping `M`:

Command:
- `mvn exec:java -Dexec.class="LSBWrap" -Dexec.args="{0} {1} {2} {3}"`

Arguments:
- `{0}`: `I` File
- `{1}`: `M` File
- `{2}`: `K` File
- `{3}`: Pathname to Store `W` (`W` Filename: `{3}/Wrapper.png`)

---

### Unwrapping `M`:

Command:
- `mvn exec:java -Dexec.class="LSBUnwrap" -Dexec.args="{0} {1} {2}"`

Arguments:
- `{0}`: `W` File
- `{1}`: `K` File
- `{2}`: Pathname to Store `M` (`M` Filename: `{2}/UnwrappedMsg.txt`)

---

| Name | University | Email |
| ---- | ---- | ---- |
| Ricardo Grade | Técnico Lisboa | ricardo.grade@tecnico.ulisboa.pt |
