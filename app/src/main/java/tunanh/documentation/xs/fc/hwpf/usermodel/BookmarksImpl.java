/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package tunanh.documentation.xs.fc.hwpf.usermodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tunanh.documentation.xs.fc.hwpf.model.BookmarksTables;
import tunanh.documentation.xs.fc.hwpf.model.GenericPropertyNode;
import tunanh.documentation.xs.fc.hwpf.model.PropertyNode;


/**
 * Implementation of user-friendly interface for document bookmarks
 * 
 * @author Sergey Vladimirov (vlsergey {at} gmail {doc} com)
 */
public class BookmarksImpl implements Bookmarks
{

    private final BookmarksTables bookmarksTables;

    private Map<Integer, List<GenericPropertyNode>> sortedDescriptors = null;

    private int[] sortedStartPositions = null;

    public BookmarksImpl(BookmarksTables bookmarksTables)
    {
        this.bookmarksTables = bookmarksTables;
    }

    private POIBookmark getBookmark(final GenericPropertyNode first)
    {
        return new BookmarkImpl(first);
    }

    public POIBookmark getBookmark(int index)
    {
        final GenericPropertyNode first = bookmarksTables.getDescriptorFirst(index);
        return getBookmark(first);
    }

    public List<POIBookmark> getBookmarksAt(int startCp)
    {
        updateSortedDescriptors();

        List<GenericPropertyNode> nodes = sortedDescriptors.get(Integer.valueOf(startCp));
        if (nodes == null || nodes.isEmpty())
            return Collections.emptyList();

        List<POIBookmark> result = new ArrayList<POIBookmark>(nodes.size());
        for (GenericPropertyNode node : nodes)
        {
            result.add(getBookmark(node));
        }
        return Collections.unmodifiableList(result);
    }

    public int getBookmarksCount()
    {
        return bookmarksTables.getDescriptorsFirstCount();
    }

    public Map<Integer, List<POIBookmark>> getBookmarksStartedBetween(int startInclusive,
        int endExclusive)
    {
        updateSortedDescriptors();

        int startLookupIndex = Arrays.binarySearch(this.sortedStartPositions, startInclusive);
        if (startLookupIndex < 0)
            startLookupIndex = -(startLookupIndex + 1);
        int endLookupIndex = Arrays.binarySearch(this.sortedStartPositions, endExclusive);
        if (endLookupIndex < 0)
            endLookupIndex = -(endLookupIndex + 1);

        Map<Integer, List<POIBookmark>> result = new LinkedHashMap<Integer, List<POIBookmark>>();
        for (int lookupIndex = startLookupIndex; lookupIndex < endLookupIndex; lookupIndex++)
        {
            int s = sortedStartPositions[lookupIndex];
            if (s < startInclusive)
                continue;
            if (s >= endExclusive)
                break;

            List<POIBookmark> startedAt = getBookmarksAt(s);
            if (startedAt != null)
                result.put(Integer.valueOf(s), startedAt);
        }

        return Collections.unmodifiableMap(result);
    }

    private void updateSortedDescriptors()
    {
        if (sortedDescriptors != null)
            return;

        Map<Integer, List<GenericPropertyNode>> result = new HashMap<Integer, List<GenericPropertyNode>>();
        for (int b = 0; b < bookmarksTables.getDescriptorsFirstCount(); b++)
        {
            GenericPropertyNode property = bookmarksTables.getDescriptorFirst(b);
            Integer positionKey = Integer.valueOf(property.getStart());
            List<GenericPropertyNode> atPositionList = result.get(positionKey);
            if (atPositionList == null)
            {
                atPositionList = new LinkedList<GenericPropertyNode>();
                result.put(positionKey, atPositionList);
            }
            atPositionList.add(property);
        }

        int counter = 0;
        int[] indices = new int[result.size()];
        for (Map.Entry<Integer, List<GenericPropertyNode>> entry : result.entrySet())
        {
            indices[counter++] = entry.getKey().intValue();
            List<GenericPropertyNode> updated = new ArrayList<GenericPropertyNode>(entry.getValue());
            Collections.sort(updated, PropertyNode.EndComparator.instance);
            entry.setValue(updated);
        }
        Arrays.sort(indices);

        this.sortedDescriptors = result;
        this.sortedStartPositions = indices;
    }

    private final class BookmarkImpl implements POIBookmark
    {
        private final GenericPropertyNode first;

        private BookmarkImpl(GenericPropertyNode first)
        {
            this.first = first;
        }

        @ Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            BookmarkImpl other = (BookmarkImpl)obj;
            if (first == null)
            {
                return other.first == null;
            }
            else return first.equals(other.first);
        }

        public int getEnd()
        {
            int currentIndex = bookmarksTables.getDescriptorFirstIndex(first);
            try
            {
                GenericPropertyNode descriptorLim = bookmarksTables.getDescriptorLim(currentIndex);
                return descriptorLim.getStart();
            }
            catch(IndexOutOfBoundsException exc)
            {
                return first.getEnd();
            }
        }

        public String getName()
        {
            int currentIndex = bookmarksTables.getDescriptorFirstIndex(first);
            try
            {
                return bookmarksTables.getName(currentIndex);
            }
            catch(ArrayIndexOutOfBoundsException exc)
            {
                return "";
            }
        }

        public int getStart()
        {
            return first.getStart();
        }

        @ Override
        public int hashCode()
        {
            return 31 + (first == null ? 0 : first.hashCode());
        }

        public void setName(String name)
        {
            int currentIndex = bookmarksTables.getDescriptorFirstIndex(first);
            bookmarksTables.setName(currentIndex, name);
        }

        @ Override
        public String toString()
        {
            return "Bookmark [" + getStart() + "; " + getEnd() + "): name: " + getName();
        }

    }
}
