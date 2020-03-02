package com.dans.service.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name="avatar_image")
public class AvatarImage {
    @Lob
    private byte[] image;
}