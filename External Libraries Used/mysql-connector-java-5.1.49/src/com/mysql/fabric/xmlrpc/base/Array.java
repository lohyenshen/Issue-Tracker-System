/*
  Copyright (c) 2013, 2014, Oracle and/or its affiliates. Projects_And_Users rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package com.mysql.fabric.xmlrpc.base;

public class Array {

    protected Data data;

    /**
     * Gets the value of the data property.
     */
    public Data getData() {
        return this.data;
    }

    /**
     * Sets the value of the data property.
     */
    public void setData(Data value) {
        this.data = value;
    }

    public void addValue(Value v) {
        if (this.data == null) {
            this.data = new Data();
        }
        this.data.addValue(v);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<array>");
        sb.append(this.data.toString());
        sb.append("</array>");
        return sb.toString();
    }
}
