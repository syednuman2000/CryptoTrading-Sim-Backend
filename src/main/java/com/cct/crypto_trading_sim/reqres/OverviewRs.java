package com.cct.crypto_trading_sim.reqres;

import com.cct.crypto_trading_sim.dto.CryptoTableDTO;
import lombok.Data;

import java.util.List;

@Data
public class OverviewRs {
    List<CryptoTableDTO> cryptoTableDTOS;
}
