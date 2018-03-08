package br.net.triangulohackerspace.westation.cucumber.stepdefs;

import br.net.triangulohackerspace.westation.WestationApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = WestationApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
