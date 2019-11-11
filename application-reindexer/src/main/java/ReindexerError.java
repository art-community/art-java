import lombok.*;

@ToString
@Getter
@AllArgsConstructor
public class ReindexerError {
    private final int code;
    private final String what;
}
