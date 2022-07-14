package com.gmail.andersoninfonet.dsmeta.services;

import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.gmail.andersoninfonet.dsmeta.entities.Sale;
import com.gmail.andersoninfonet.dsmeta.repositories.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

	private final SaleRepository saleRepository;

	/**
	 * @param saleRepository
	 */
	public SmsService(SaleRepository saleRepository) {
		this.saleRepository = saleRepository;
	}

	@Value("${twilio.sid}")
	private String twilioSid;

	@Value("${twilio.key}")
	private String twilioKey;

	@Value("${twilio.phone.from}")
	private String twilioPhoneFrom;

	@Value("${twilio.phone.to}")
	private String twilioPhoneTo;

	@Async
	public void sendSms(Long saleId) {

		Twilio.init(twilioSid, twilioKey);

		PhoneNumber to = new PhoneNumber(twilioPhoneTo);
		PhoneNumber from = new PhoneNumber(twilioPhoneFrom);
		var objFormattedMsg = new Object(){String msg = "";};
		var objMessage = new Object(){ Optional<Message> message = Optional.empty();};

		saleRepository.findById(saleId)
		.ifPresentOrElse(
			sale -> { 
				objFormattedMsg.msg = formatMessage(sale);
				objMessage.message = Optional.ofNullable(Message.creator(to, from, objFormattedMsg.msg).create());
			}, 
			() -> {throw new EntityExistsException("Usuario não encontrado para o id "+saleId);});

		objMessage.message
		.ifPresentOrElse(
			msg -> System.out.println(msg.getSid()), 
			() -> System.out.println("Erro ao tentar enviar SMS para o vendedor de id " + saleId));
	}

	private String formatMessage(Sale sale) {
		return """
				O vendedor %s foi destaque em %s com um total de R$ %s
				""".formatted(
					sale.getSellerName(), 
					sale.getDate().getMonthValue() + "/" + sale.getDate().getYear(), 
					String.format("%.2f", sale.getAmount()));
	}
}
