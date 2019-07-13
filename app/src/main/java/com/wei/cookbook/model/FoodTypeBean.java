package com.wei.cookbook.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class FoodTypeBean
{
    /**
     * parentId : 10001
     * name : 菜式菜品
     * list : [{"id":"1","name":"家常菜","parentId":"10001"},{"id":"2","name":"快手菜","parentId":"10001"},{"id":"3","name":"创意菜","parentId":"10001"},{"id":"4","name":"素菜","parentId":"10001"},{"id":"5","name":"凉菜","parentId":"10001"},{"id":"6","name":"烘焙","parentId":"10001"},{"id":"7","name":"面食","parentId":"10001"},{"id":"8","name":"汤","parentId":"10001"},{"id":"9","name":"自制调味料","parentId":"10001"}]
     */

    private String parentId;
    private String name;
    private List<ListBean> list;

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<ListBean> getList()
    {
        return list;
    }

    public void setList(List<ListBean> list)
    {
        this.list = list;
    }

    public static class ListBean implements Parcelable
    {
        /**
         * id : 1
         * name : 家常菜
         * parentId : 10001
         */

        private int id;
        private String name;
        private String parentId;


        protected ListBean(Parcel in)
        {
            id = in.readInt();
            name = in.readString();
            parentId = in.readString();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>()
        {
            @Override
            public ListBean createFromParcel(Parcel in)
            {
                return new ListBean(in);
            }

            @Override
            public ListBean[] newArray(int size)
            {
                return new ListBean[size];
            }
        };

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getParentId()
        {
            return parentId;
        }

        public void setParentId(String parentId)
        {
            this.parentId = parentId;
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(parentId);
        }
    }
}
