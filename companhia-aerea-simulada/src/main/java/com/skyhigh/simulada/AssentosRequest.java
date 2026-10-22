package com.skyhigh.simulada;

import jakarta.validation.constraints.Min;

public record AssentosRequest(@Min(value = 1, message = "quantidade mínima é 1") int quantidade) {
}
