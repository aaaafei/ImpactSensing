package com.jsti.tunnel.monitor.bean;

import lombok.Data;

@Data
public class TmBridgeBaseinfo {

    private String uuid;

    private Integer sortno;

    private String lineCode;

    private String lineName;

    private String sectionName;

    private String basicType;

    private String basicMileage;

    private String basicLength;

    private Integer pierTotalCount;

    private Integer pierNormalCount;

    private Integer pierTransroadCount;

    private Integer pierTranswaterCount;

    private Integer padTotalCount;

    private Integer padBasinTypeCount;

    private Integer padPlateTypeCount;

    private Integer padBallTypeCount;

    private Integer accessoryExpansionJointCount;

    private String soundBarrierMileage;

    private String soundBarrierPosition;

    private String soundBarrierType;

    private String shieldedBoardSlabMileage;

    private String shieldedBoardSlabType;

    private String shieldedBoardAlcMileage;

    private String shieldedBoardAlcType;

    private String shieldedBoardMetalMileage;

    private String shieldedBoardMetalType;

    private Integer antiCollisionHeightLimitation;

    private Integer antiCollisionSlab;

    private Integer antiCollisionRack;

    private Integer antiCollisionLabel;

    private Integer antiWall;

    private Integer maintenancePlatformCount;

    private String maintenancePlatformPier;
}
