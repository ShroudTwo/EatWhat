package com.wei.cookbook.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.wei.cookbook.App;
import com.wei.cookbook.sql.FoodBeanDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;


@Entity
public class FoodBean implements Parcelable
{
    /**
     * id : 909
     * title : 泰式柠檬蒸鲈鱼
     * tags : 家常菜;私房菜;海鲜类;美容;瘦身;健脾开胃;护肝;老年人;运动员;骨质疏松;辣;蒸;简单;抗疲劳;鲜;香;孕妇;消化不良;开胃;减肥;柠檬味;补水;补钙;促消化;祛斑;产妇;1-2人;生津止渴;肥胖;养肝护肝;补肝;蒸锅;中等难度;鲈;保湿;增高;晕车
     * imtro : 菜谱来自电视节目：中华美食频道的《千味坊》 JIMMY老师教的菜，都是一些简单又美味的家常菜，这几天每天中午12点都会收看他的节目。 JIMMY老师教大家怎么看鱼是否新鲜,如果蒸出来后鱼的眼珠是鼓出来的就是新鲜 的.相反眼珠藏在里面就代表不新鲜了.
     * ingredients : 鲈鱼,1个;柠檬,2个;红椒,6个
     * burden : 大蒜头,适量;香菜,适量;盐,适量;生姜,适量
     * albums : ["http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/t/1/909_135871.jpg"]
     * steps : [{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_70d5525103c69d8a.jpg","step":"1.鲈鱼一条，开肚洗净"},{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_3f4a6e5a5ae225ca.jpg","step":"2.柠檬2-3个，生姜一小块，大蒜头，香菜，辣辣的红椒六个"},{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_c8fe915ee6ff3a4c.jpg","step":"3.把鱼切块，用少量盐，料酒腌一下。"},{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_79faf1f277616c40.jpg","step":"4.红椒、大蒜、生姜切碎，香菜切碎"},{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_79083ec44dd9406c.jpg","step":"5.把柠檬汁挤出用小碗盛着，放入调味料：鱼露、精盐、鸡精、白糖（多一些白糖）沾一点尝尝，汁不要太酸也不要太甜。"},{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_c0b48c233c6fd724.jpg","step":"6.接着把鱼码成形，倒入调好味的柠檬汁，铺上红椒、大蒜、生姜碎。"},{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_66c8b71544b8abde.jpg","step":"7.锅内烧开水，把鱼放上去蒸（大火7分钟即可）"},{"img":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_b57104f06672bf2b.jpg","step":"8.蒸好后，，撒上绿色的香菜叶（记住哦！！！这道菜不用放油的哦）"}]
     */

    private String id;
    @Id
    private String title;
    private String tags;
    private String imtro;
    private String ingredients;
    private String burden;
    @Transient
    private List<String> albums;
    @Transient
    private List<StepsBean> steps;
    private String imageUrls;


    @Generated(hash = 1875402827)
    public FoodBean(String id, String title, String tags, String imtro, String ingredients, String burden, String imageUrls)
    {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.imtro = imtro;
        this.ingredients = ingredients;
        this.burden = burden;
        this.imageUrls = imageUrls;
    }


    @Generated(hash = 895705851)
    public FoodBean()
    {
    }


    protected FoodBean(Parcel in)
    {
        id = in.readString();
        title = in.readString();
        tags = in.readString();
        imtro = in.readString();
        ingredients = in.readString();
        burden = in.readString();
        albums = in.createStringArrayList();
        steps = in.createTypedArrayList(StepsBean.CREATOR);
        imageUrls = in.readString();
    }

    public static final Creator<FoodBean> CREATOR = new Creator<FoodBean>()
    {
        @Override
        public FoodBean createFromParcel(Parcel in)
        {
            return new FoodBean(in);
        }

        @Override
        public FoodBean[] newArray(int size)
        {
            return new FoodBean[size];
        }
    };

    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getTags()
    {
        return this.tags;
    }


    public void setTags(String tags)
    {
        this.tags = tags;
    }


    public String getImtro()
    {
        return this.imtro;
    }


    public void setImtro(String imtro)
    {
        this.imtro = imtro;
    }


    public String getIngredients()
    {
        return this.ingredients;
    }


    public void setIngredients(String ingredients)
    {
        this.ingredients = ingredients;
    }


    public String getBurden()
    {
        return this.burden;
    }


    public void setBurden(String burden)
    {
        this.burden = burden;
    }

    public List<String> getAlbums()
    {
        return albums;
    }

    public void setAlbums(List<String> albums)
    {
        this.albums = albums;
    }

    public List<StepsBean> getSteps()
    {
        return steps;
    }

    public void setSteps(List<StepsBean> steps)
    {
        this.steps = steps;
    }

    public String getImageUrls()
    {
        return this.imageUrls;
    }


    public void setImageUrls(String imageUrls)
    {
        this.imageUrls = imageUrls;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(tags);
        dest.writeString(imtro);
        dest.writeString(ingredients);
        dest.writeString(burden);
        dest.writeStringList(albums);
        dest.writeTypedList(steps);
        dest.writeString(imageUrls);
    }


    public static class StepsBean implements Parcelable
    {
        /**
         * img : http://juheimg.oss-cn-hangzhou.aliyuncs.com/cookbook/s/10/909_70d5525103c69d8a.jpg
         * step : 1.鲈鱼一条，开肚洗净
         */

        private String img;
        private String step;

        protected StepsBean(Parcel in)
        {
            img = in.readString();
            step = in.readString();
        }

        public static final Creator<StepsBean> CREATOR = new Creator<StepsBean>()
        {
            @Override
            public StepsBean createFromParcel(Parcel in)
            {
                return new StepsBean(in);
            }

            @Override
            public StepsBean[] newArray(int size)
            {
                return new StepsBean[size];
            }
        };

        public String getImg()
        {
            return img;
        }

        public void setImg(String img)
        {
            this.img = img;
        }

        public String getStep()
        {
            return step;
        }

        public void setStep(String step)
        {
            this.step = step;
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(img);
            dest.writeString(step);
        }
    }


    /*验证当前菜品是否已被收藏*/
    public boolean isCollected()
    {
        FoodBeanDao dao = App.mSession.getFoodBeanDao();
        FoodBean data = dao.queryBuilder().where(FoodBeanDao.Properties.Id.eq(this.id))
                .unique();
        return data != null;
    }

    /*收藏该条菜品*/
    public void collect()
    {
        FoodBeanDao dao = App.mSession.getFoodBeanDao();
        this.imageUrls = (this.albums != null && this.albums.size() != 0) ? this.albums.get(0) : "";
        dao.insertOrReplace(this);
    }

    /*取消收藏该条菜品*/
    public void cancel()
    {
        FoodBeanDao dao = App.mSession.getFoodBeanDao();
        dao.delete(this);
    }
}
