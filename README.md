# IMG-Steganography

Context:
- `M`: Secret Message
- `I`: Image where `M` is gonna be Wrapped
- `W`: Image where `M` is Wrapped
- `K`: Key to Wrap `M` into `I` & to Unwrap `M` out of `W`

Functionalities:
- Wrap `M` into `W` using a `K` randomly generated.
- Unwrap `M` out of `W` using the same `K`.

---

### Wrapping `M`:

Command:
- `mvn exec:java -Dexec.class="LSBWrap" -Dexec.args="{0} {1} {2}"`

Arguments:
- `{0}`: `I` File
- `{1}`: `M` File
- `{2}`: Pathname to Store `W` & `K` (`W` Filename: `{2}/Wrapper.png`, `K` Filename: `{2}/LSB.key`)

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

