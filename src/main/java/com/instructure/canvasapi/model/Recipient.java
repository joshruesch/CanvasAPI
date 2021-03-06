package com.instructure.canvasapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Josh Ruesch
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */

public class Recipient extends CanvasComparable<Recipient> implements Parcelable {

    public enum Type {group, metagroup, person};

    private String id;
    private int user_count;
    private int item_count;
    private String name;
    private String avatar_url;

    ///////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////
    public String getStringId(){
        return id;
    }
    @Override
    public Date getComparisonDate() {
        return null;
    }

    @Override
    public String getComparisonString() {
        return id;
    }

    public int getUser_count() {
		return user_count;
	}
	public String getName() {
		return name;
	}
	public Type getRecipientType() {

        try{
            long tempId = Long.parseLong(id);
            return Type.person;
        }
        catch(Exception E){}

        if(user_count > 0){
           return Type.group;
        }

		return Type.metagroup;
	}
	public int getItemCount() {
		return item_count;
	}

    public String getAvatarURL() {
        return avatar_url;
    }
    public void setAvatarURL(String avatar) {
        this.avatar_url = avatar;
    }

	///////////////////////////////////////////////////////////////////////////
	// Parcelable
	///////////////////////////////////////////////////////////////////////////

    public Recipient(Parcel p)
    {
        readFromParcel(p);
    }

    public Recipient(String _id, String _name, int _userCount, int _itemCount, int _enum) {
        id = _id;
        name = _name;

        user_count = _userCount;
        item_count = _itemCount;

    }
	
	public static final Parcelable.Creator<Recipient> CREATOR
	        = new Parcelable.Creator<Recipient>() {

		public Recipient createFromParcel(Parcel in) {
			return new Recipient(in);
		}

		public Recipient[] newArray(int size) {
			return new Recipient[size];
		}
	};

	public static int recipientTypeToInt(Type t)
	{
		if(t == Type.group)
			return 0;
		else if (t == Type.metagroup)
			return 1;
		else if (t == Type.person)
			return 2;
		else
			return -1;
	}

	public static Type intToRecipientType(int i)
	{
		if(i == 0)
			return Type.group;
		else if (i == 1)
			return Type.metagroup;
		else if (i == 2)
			return Type.person;
		else
			return null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in)
	{
		id = in.readString();
		user_count = in.readInt();
		item_count = in.readInt();
		name = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeInt(user_count);
		dest.writeInt(item_count);
		dest.writeString(name);
	}

    ///////////////////////////////////////////////////////////////////////////
    // Overrides
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        Recipient other = (Recipient) obj;

        return compareTo(other) == 0;
    }

    @Override
    public String toString(){
        return name;
    }

}
