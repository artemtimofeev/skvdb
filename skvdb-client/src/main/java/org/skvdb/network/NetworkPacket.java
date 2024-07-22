package org.skvdb.network;

import org.skvdb.dto.Dto;

public record NetworkPacket(String dtoType, String encodedJsonBody) implements Dto {
}
