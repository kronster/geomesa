/***********************************************************************
 * Copyright (c) 2013-2022 Commonwealth Computer Research, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php.
 ***********************************************************************/

package org.locationtech.geomesa.kafka.confluent

import org.apache.avro.Schema
import org.locationtech.geomesa.features.SerializationType.SerializationType
import org.locationtech.geomesa.kafka.utils.GeoMessage.{Change, Clear, Delete}
import org.locationtech.geomesa.kafka.utils.GeoMessageSerializer.GeoMessageSerializerFactory
import org.locationtech.geomesa.kafka.utils.{GeoMessage, GeoMessageSerializer}
import org.opengis.feature.simple.SimpleFeatureType

import java.net.URL
import java.nio.charset.StandardCharsets

class ConfluentGeoMessageSerializer(sft: SimpleFeatureType, serializer: ConfluentFeatureSerializer)
    extends GeoMessageSerializer(sft, null, null, null, 0) {

  override def serialize(msg: GeoMessage): (Array[Byte], Array[Byte], Map[String, Array[Byte]]) =
    throw new NotImplementedError("Confluent data store is read-only")

  override def deserialize(
      key: Array[Byte],
      value: Array[Byte],
      headers: Map[String, Array[Byte]],
      timestamp: Long): GeoMessage = {
    if (key.isEmpty) { Clear } else {
      val id = new String(key, StandardCharsets.UTF_8)
      if (value == null) { Delete(id) } else { Change(serializer.deserialize(id, value)) }
    }
  }
}

object ConfluentGeoMessageSerializer {
  class ConfluentGeoMessageSerializerFactory(
    schemaRegistryUrl: URL,
    schemaOverrides: Map[String, Schema]
  ) extends GeoMessageSerializerFactory {
    override def apply(
        sft: SimpleFeatureType,
        serialization: SerializationType,
        `lazy`: Boolean): GeoMessageSerializer = {
      val serializer = ConfluentFeatureSerializer.builder(sft, schemaRegistryUrl, schemaOverrides.get(sft.getTypeName))
        .withoutId.withUserData.build()
      new ConfluentGeoMessageSerializer(sft, serializer)
    }
  }
}
