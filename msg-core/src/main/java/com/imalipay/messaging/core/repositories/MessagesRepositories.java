package com.imalipay.messaging.core.repositories;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.imalipay.messaging.core.models.Messages;

public interface MessagesRepositories extends JpaRepository<Messages, BigInteger>,
	JpaSpecificationExecutor<Messages>
{

}
