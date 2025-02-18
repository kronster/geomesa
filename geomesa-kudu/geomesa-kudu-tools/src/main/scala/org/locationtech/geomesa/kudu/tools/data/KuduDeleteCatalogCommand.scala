/***********************************************************************
 * Copyright (c) 2013-2022 Commonwealth Computer Research, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php.
 ***********************************************************************/

package org.locationtech.geomesa.kudu.tools.data

import com.beust.jcommander.Parameters
import org.locationtech.geomesa.kudu.data.KuduDataStore
import org.locationtech.geomesa.kudu.tools.KuduDataStoreCommand
import org.locationtech.geomesa.kudu.tools.KuduDataStoreCommand.KuduParams
import org.locationtech.geomesa.kudu.tools.data.KuduDeleteCatalogCommand.KuduDeleteCatalogParams
import org.locationtech.geomesa.tools.data.{DeleteCatalogCommand, DeleteCatalogParams}

class KuduDeleteCatalogCommand extends DeleteCatalogCommand[KuduDataStore] with KuduDataStoreCommand {
  override val params = new KuduDeleteCatalogParams
}

object KuduDeleteCatalogCommand {
  @Parameters(commandDescription = "Delete a GeoMesa catalog completely (and all features in it)")
  class KuduDeleteCatalogParams extends DeleteCatalogParams with KuduParams
}
