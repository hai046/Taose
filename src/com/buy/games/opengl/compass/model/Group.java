/**
 * Copyright 2010 Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.buy.games.opengl.compass.model;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Group is a mesh with no mesh data itself but can contain one or more other
 * meshes.
 * 
 * @author Per-Erik Bergman (per-erik.bergman@jayway.com)
 * 
 */
public class Group extends Mesh {
	private final ArrayList<Mesh> mChildren = new ArrayList<Mesh>();
	@Override
	public void draw(GL10 gl) {
		int size = mChildren.size();
	       // Counter-clockwise winding.
        gl.glFrontFace( GL10.GL_CCW );
        // Enable face culling.
        gl.glEnable( GL10.GL_CULL_FACE );
        // What faces to remove with the face culling.
        gl.glCullFace( GL10.GL_BACK );
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
        gl.glColor4f( 1f, 1f, 1f, 1f );
		for (int i = 0; i < size; i++)
		{
			mChildren.get(i).draw(gl);
		}
        // Disable the vertices buffer.
        gl.glDisableClientState( GL10.GL_VERTEX_ARRAY );
        
        // ... end new part.
        // Disable face culling.
        gl.glDisable( GL10.GL_CULL_FACE );
	}

	/**
	 * @param location
	 * @param object
	 * @see java.util.Vector#add(int, java.lang.Object)
	 */
	public void add(int location, Mesh object) {
		mChildren.add(location, object);
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.Vector#add(java.lang.Object)
	 */
	public boolean add(Mesh object) {
		return mChildren.add(object);
	}

	/**
	 * 
	 * @see java.util.Vector#clear()
	 */
	public void clear() {
		mChildren.clear();
	}

	/**
	 * @param location
	 * @return
	 * @see java.util.Vector#get(int)
	 */
	public Mesh get(int location) {
		return mChildren.get(location);
	}

	/**
	 * @param location
	 * @return
	 * @see java.util.Vector#remove(int)
	 */
	public Mesh remove(int location) {
		return mChildren.remove(location);
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.Vector#remove(java.lang.Object)
	 */
	public boolean remove(Object object) {
		return mChildren.remove(object);
	}

	/**
	 * @return
	 * @see java.util.Vector#size()
	 */
	public int size() {
		return mChildren.size();
	}

}