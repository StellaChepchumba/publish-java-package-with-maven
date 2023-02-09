package com.imalipay.messaging.dlr.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.imalipay.messaging.dlr.models.Messages;

public interface MessagesRepositories extends JpaRepository<Messages, UUID>,
	JpaSpecificationExecutor<Messages>
{

}
