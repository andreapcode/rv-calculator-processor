package com.cnh.rvcalculatorprocessor.function;


import com.cnh.rvcalculatorprocessor.dto.FinancePlusRequestDTO;
import com.cnh.rvcalculatorprocessor.dto.QlikSenseResponseDTO;
import com.cnh.rvcalculatorprocessor.dto.RequestDTO;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import java.util.Optional;


public class RVCalculatorProcessorAzureHandler extends FunctionInvoker<RequestDTO<FinancePlusRequestDTO>, HttpResponseMessage> {

    @FunctionName("rvCalculatorProcessor")
    public HttpResponseMessage execute(
            @HttpTrigger(name = "rvCalculatorRequest", methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<FinancePlusRequestDTO>> request,
            ExecutionContext context) {
        RequestDTO<FinancePlusRequestDTO> customRequest = new RequestDTO<>(request);
        if (request != null){
            context.getLogger().info("Request ok");
        }
        context.getLogger().info(request.getBody().toString());
        context.getLogger().info("Calling Azure Function RV Calculator REST API...");
        return handleRequest(customRequest, context);

    }

    @FunctionName("rvCalculatorProcessor")
    public void serviceBusProcess(
            @ServiceBusQueueTrigger(name = "msg",
                    queueName = "myqueuename",
                    connection = "myconnvarname") HttpRequestMessage<Optional<FinancePlusRequestDTO>> request,
            @QueueOutput(name = "output",
                    queueName = "test-servicebusqueuesingle-java",
                    connection = "AzureWebJobsStorage") OutputBinding<QlikSenseResponseDTO> output,
            final ExecutionContext context
    ) {
        context.getLogger().info("Calling Azure Function Receiver RV Calculator Processor...");

        output.setValue(new QlikSenseResponseDTO());
    }


/*@FunctionName("httpToServiceBusQueue")
    @ServiceBusQueueOutput(name = "message", queueName = "myqueue", connection = "AzureServiceBusConnection")
    public String pushToQueue(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
            final FinancePlusRequestDTO req,
            @HttpOutput(name = "response") final OutputBinding<FinancePlusRequestDTO> result ) {
        result.setValue(message + " has been sent.");
        return message;
    }*/
}
