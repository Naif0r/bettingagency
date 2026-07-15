package ro.ugal.aciee.bettingagency.dto;

import ro.ugal.aciee.bettingagency.model.Rate;

public record MatchOddDTO(
        String team1Name,
        String team2Name,
        Rate rate
) {
}
