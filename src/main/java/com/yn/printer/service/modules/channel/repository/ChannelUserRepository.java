package com.yn.printer.service.modules.channel.repository;


import com.yn.printer.service.modules.channel.entity.ChannelUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelUserRepository extends JpaRepository<ChannelUser, Long> {

  long countByContactPhone(String contactPhone);

    ChannelUser findFirstByContactPhone(String phoneNumber);
}
