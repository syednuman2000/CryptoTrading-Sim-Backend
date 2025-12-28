package com.cct.crypto_trading_sim.reqres;

import com.cct.crypto_trading_sim.dto.WalletStatementDTO;
import lombok.Data;

import java.util.List;

@Data
public class WalletStatementRs {
    List<WalletStatementDTO> statements;
}
