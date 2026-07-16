package ro.ugal.aciee.bettingagency.dto;

import ro.ugal.aciee.bettingagency.model.Match;
import ro.ugal.aciee.bettingagency.model.enums.RateType;

public record BetLegDTO(
        Match match,
        String team1Name,
        String team2Name,
        RateType rateType
) {
}
