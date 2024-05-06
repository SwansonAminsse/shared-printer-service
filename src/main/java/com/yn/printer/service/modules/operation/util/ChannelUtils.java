package com.yn.printer.service.modules.operation.util;

import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.channel.entity.ChannelUser;
import com.yn.printer.service.modules.channel.repository.ChannelPartnerRepository;
import com.yn.printer.service.modules.channel.repository.ChannelUserRepository;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.settlement.service.ISettlementDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChannelUtils  {
    @Autowired
    private  ChannelPartnerRepository channelPartnerRepository;
    @Autowired
    private  ChannelUserRepository channelUserRepository;
    @Autowired
    ISettlementDetailsService settlementDetailsService;
    public ChannelPartner getChannelPartner(Member member) {
        String phoneNumber = member.getPhoneNumber();
        ChannelUser channelUser = channelUserRepository.findFirstByContactPhone(phoneNumber);
        if (channelUser == null) {
            settlementDetailsService.addUser(member.getName(), member.getPhoneNumber());
            channelUser = channelUserRepository.findFirstByContactPhone(phoneNumber);
        }
        return channelUser.getChannelPartner();
    }
}